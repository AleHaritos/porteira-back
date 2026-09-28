package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.LoginRequest;
import com.example.fazendalosardo.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
}