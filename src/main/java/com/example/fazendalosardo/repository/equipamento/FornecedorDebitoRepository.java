package com.example.fazendalosardo.repository.equipamento;

import com.example.fazendalosardo.dto.equipamento.FornecedorDebitoRow;
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
public class FornecedorDebitoRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final String UNION_SQL = """
        SELECT 'VEICULO' AS origem, m.id AS id, v.nome AS nome_ativo, m.tipo AS tipo,
               m.descricao AS descricao, m.data AS data, m.valor AS valor,
               m.status_pagamento AS status_pagamento, m.data_baixa AS data_baixa
        FROM manutencoes m
        JOIN veiculos v ON v.id = m.veiculo_id
        WHERE m.fornecedor_id = ? AND m.status_pagamento = ? AND m.ativo = true
          AND m.data >= ? AND m.data <= ?

        UNION ALL

        SELECT 'EQUIPAMENTO' AS origem, me.id AS id, e.nome AS nome_ativo, me.tipo AS tipo,
               me.descricao AS descricao, me.data AS data, me.valor AS valor,
               me.status_pagamento AS status_pagamento, me.data_baixa AS data_baixa
        FROM manutencoes_equipamento me
        JOIN equipamentos e ON e.id = me.equipamento_id
        WHERE me.fornecedor_id = ? AND me.status_pagamento = ? AND me.ativo = true
          AND me.data >= ? AND me.data <= ?

        UNION ALL

        SELECT 'CONTA' AS origem, cf.id AS id, f.nome AS nome_ativo, 'OUTRA' AS tipo,
               cf.descricao AS descricao, cf.data AS data, cf.valor AS valor,
               cf.status_pagamento AS status_pagamento, cf.data_baixa AS data_baixa
        FROM contas_fornecedor cf
        JOIN fornecedores f ON f.id = cf.fornecedor_id
        WHERE cf.fornecedor_id = ? AND cf.status_pagamento = ?
          AND cf.data >= ? AND cf.data <= ?

        UNION ALL

        SELECT 'MANUTENCAO_PARCELADA' AS origem, p.id AS id, COALESCE(v2.nome, e2.nome) AS nome_ativo, mp.tipo AS tipo,
               COALESCE(mp.descricao, 'Manutenção') || ' (Parcela ' || p.numero_parcela || '/' || mp.numero_parcelas || ')' AS descricao,
               p.data_vencimento AS data, p.valor AS valor,
               p.status AS status_pagamento, p.data_baixa AS data_baixa
        FROM parcelas_manutencao p
        JOIN manutencoes_parceladas mp ON mp.id = p.manutencao_parcelada_id
        LEFT JOIN veiculos v2 ON v2.id = mp.veiculo_id
        LEFT JOIN equipamentos e2 ON e2.id = mp.equipamento_id
        WHERE mp.fornecedor_id = ? AND p.status = ? AND mp.ativo = true
          AND p.data_vencimento >= ? AND p.data_vencimento <= ?
        """;

    public Page<FornecedorDebitoRow> buscarPorFornecedorEStatus(Long fornecedorId, String status, LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        String sqlPaginado = UNION_SQL + " ORDER BY data DESC LIMIT ? OFFSET ?";

        List<FornecedorDebitoRow> conteudo = jdbcTemplate.query(
                sqlPaginado,
                (rs, rowNum) -> new FornecedorDebitoRow(
                        rs.getString("origem"),
                        rs.getLong("id"),
                        rs.getString("nome_ativo"),
                        rs.getString("tipo"),
                        rs.getString("descricao"),
                        rs.getObject("data", LocalDate.class),
                        rs.getBigDecimal("valor"),
                        rs.getString("status_pagamento"),
                        rs.getObject("data_baixa", LocalDate.class)
                ),
                fornecedorId, status, dataInicio, dataFim,
                fornecedorId, status, dataInicio, dataFim,
                fornecedorId, status, dataInicio, dataFim,
                fornecedorId, status, dataInicio, dataFim,
                pageable.getPageSize(), pageable.getOffset()
        );

        String sqlContagem = "SELECT COUNT(*) FROM (" + UNION_SQL + ") AS total";
        Long total = jdbcTemplate.queryForObject(
                sqlContagem, Long.class,
                fornecedorId, status, dataInicio, dataFim,
                fornecedorId, status, dataInicio, dataFim,
                fornecedorId, status, dataInicio, dataFim,
                fornecedorId, status, dataInicio, dataFim
        );

        return new PageImpl<>(conteudo, pageable, total != null ? total : 0);
    }
}