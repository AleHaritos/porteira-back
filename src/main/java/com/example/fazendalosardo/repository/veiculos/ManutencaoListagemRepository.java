package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.dto.veiculos.ManutencaoListagemRow;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ManutencaoListagemRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String UNION_SQL_VEICULO = """
            SELECT 'SIMPLES' AS origem, m.id AS id, v.nome AS ativo_nome, m.tipo AS tipo,
                   m.descricao AS descricao, m.data AS data, m.valor AS valor, m.km_atual AS km_atual,
                   m.forma_pagamento AS forma_pagamento, f.nome AS fornecedor_nome,
                   COALESCE(n.nome, s.nome) AS vinculo_nome,
                   m.status_pagamento AS status_pagamento, m.data_baixa AS data_baixa,
                   CAST(NULL AS integer) AS numero_parcela, CAST(NULL AS integer) AS total_parcelas
            FROM manutencoes m
            JOIN veiculos v ON v.id = m.veiculo_id
            LEFT JOIN fornecedores f ON f.id = m.fornecedor_id
            LEFT JOIN negocio n ON n.id = m.negocio_id
            LEFT JOIN safras s ON s.id = m.safra_id
            WHERE m.fazenda_id = ? AND m.ativo = true
              AND (CAST(? AS bigint) IS NULL OR m.veiculo_id = ?)
              AND (CAST(? AS date) IS NULL OR m.data >= ?)
              AND (CAST(? AS date) IS NULL OR m.data <= ?)

            UNION ALL

            SELECT 'PARCELA' AS origem, p.id AS id, v2.nome AS ativo_nome, mp.tipo AS tipo,
                   mp.descricao AS descricao, p.data_vencimento AS data, p.valor AS valor, mp.km_atual AS km_atual,
                   'FORNECEDOR_CREDENCIADO' AS forma_pagamento, fo.nome AS fornecedor_nome,
                   COALESCE(n2.nome, s2.nome) AS vinculo_nome,
                   p.status AS status_pagamento, p.data_baixa AS data_baixa,
                   p.numero_parcela AS numero_parcela, mp.numero_parcelas AS total_parcelas
            FROM parcelas_manutencao p
            JOIN manutencoes_parceladas mp ON mp.id = p.manutencao_parcelada_id
            JOIN veiculos v2 ON v2.id = mp.veiculo_id
            JOIN fornecedores fo ON fo.id = mp.fornecedor_id
            LEFT JOIN negocio n2 ON n2.id = mp.negocio_id
            LEFT JOIN safras s2 ON s2.id = mp.safra_id
            WHERE mp.fazenda_id = ? AND mp.ativo = true AND mp.veiculo_id IS NOT NULL
              AND (CAST(? AS bigint) IS NULL OR mp.veiculo_id = ?)
              AND (CAST(? AS date) IS NULL OR p.data_vencimento >= ?)
              AND (CAST(? AS date) IS NULL OR p.data_vencimento <= ?)
            """;

    private static final String UNION_SQL_EQUIPAMENTO = """
            SELECT 'SIMPLES' AS origem, m.id AS id, e.nome AS ativo_nome, m.tipo AS tipo,
                   m.descricao AS descricao, m.data AS data, m.valor AS valor, CAST(NULL AS integer) AS km_atual,
                   m.forma_pagamento AS forma_pagamento, f.nome AS fornecedor_nome,
                   COALESCE(n.nome, s.nome) AS vinculo_nome,
                   m.status_pagamento AS status_pagamento, m.data_baixa AS data_baixa,
                   CAST(NULL AS integer) AS numero_parcela, CAST(NULL AS integer) AS total_parcelas
            FROM manutencoes_equipamento m
            JOIN equipamentos e ON e.id = m.equipamento_id
            LEFT JOIN fornecedores f ON f.id = m.fornecedor_id
            LEFT JOIN negocio n ON n.id = m.negocio_id
            LEFT JOIN safras s ON s.id = m.safra_id
            WHERE m.fazenda_id = ? AND m.ativo = true
              AND (CAST(? AS bigint) IS NULL OR m.equipamento_id = ?)
              AND (CAST(? AS date) IS NULL OR m.data >= ?)
              AND (CAST(? AS date) IS NULL OR m.data <= ?)

            UNION ALL

            SELECT 'PARCELA' AS origem, p.id AS id, e2.nome AS ativo_nome, mp.tipo AS tipo,
                   mp.descricao AS descricao, p.data_vencimento AS data, p.valor AS valor, CAST(NULL AS integer) AS km_atual,
                   'FORNECEDOR_CREDENCIADO' AS forma_pagamento, fo.nome AS fornecedor_nome,
                   COALESCE(n2.nome, s2.nome) AS vinculo_nome,
                   p.status AS status_pagamento, p.data_baixa AS data_baixa,
                   p.numero_parcela AS numero_parcela, mp.numero_parcelas AS total_parcelas
            FROM parcelas_manutencao p
            JOIN manutencoes_parceladas mp ON mp.id = p.manutencao_parcelada_id
            JOIN equipamentos e2 ON e2.id = mp.equipamento_id
            JOIN fornecedores fo ON fo.id = mp.fornecedor_id
            LEFT JOIN negocio n2 ON n2.id = mp.negocio_id
            LEFT JOIN safras s2 ON s2.id = mp.safra_id
            WHERE mp.fazenda_id = ? AND mp.ativo = true AND mp.equipamento_id IS NOT NULL
              AND (CAST(? AS bigint) IS NULL OR mp.equipamento_id = ?)
              AND (CAST(? AS date) IS NULL OR p.data_vencimento >= ?)
              AND (CAST(? AS date) IS NULL OR p.data_vencimento <= ?)
            """;

    public Page<ManutencaoListagemRow> buscarPorFazendaVeiculo(Long fazendaId, Long veiculoId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        return executar(UNION_SQL_VEICULO, fazendaId, veiculoId, dataInicio, dataFim, pageable);
    }

    public Page<ManutencaoListagemRow> buscarPorFazendaEquipamento(Long fazendaId, Long equipamentoId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        return executar(UNION_SQL_EQUIPAMENTO, fazendaId, equipamentoId, dataInicio, dataFim, pageable);
    }

    private Page<ManutencaoListagemRow> executar(String unionSql, Long fazendaId, Long ativoId, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        String sqlPaginado = unionSql + " ORDER BY data DESC LIMIT ? OFFSET ?";

        List<ManutencaoListagemRow> conteudo = jdbcTemplate.query(
                sqlPaginado,
                (rs, rowNum) -> new ManutencaoListagemRow(
                        rs.getString("origem"),
                        rs.getLong("id"),
                        rs.getString("ativo_nome"),
                        rs.getString("tipo"),
                        rs.getString("descricao"),
                        rs.getObject("data", LocalDate.class),
                        rs.getBigDecimal("valor"),
                        (Integer) rs.getObject("km_atual"),
                        rs.getString("forma_pagamento"),
                        rs.getString("fornecedor_nome"),
                        rs.getString("vinculo_nome"),
                        rs.getString("status_pagamento"),
                        rs.getObject("data_baixa", LocalDate.class),
                        (Integer) rs.getObject("numero_parcela"),
                        (Integer) rs.getObject("total_parcelas")
                ),
                fazendaId, ativoId, ativoId, dataInicio, dataInicio, dataFim, dataFim,
                fazendaId, ativoId, ativoId, dataInicio, dataInicio, dataFim, dataFim,
                pageable.getPageSize(), pageable.getOffset()
        );

        String sqlContagem = "SELECT COUNT(*) FROM (" + unionSql + ") AS total";
        Long total = jdbcTemplate.queryForObject(
                sqlContagem, Long.class,
                fazendaId, ativoId, ativoId, dataInicio, dataInicio, dataFim, dataFim,
                fazendaId, ativoId, ativoId, dataInicio, dataInicio, dataFim, dataFim
        );

        return new PageImpl<>(conteudo, pageable, total != null ? total : 0);
    }
}