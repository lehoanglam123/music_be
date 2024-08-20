package com.spring.security.service.impl;

import com.spring.security.entity.AuthenticationRequest;
import com.spring.security.entity.RegisterRequest;
import com.spring.security.model.Account;
import com.spring.security.model.Role;
import com.spring.security.model.Token;
import com.spring.security.model.TokenType;
import com.spring.security.repository.AccountRepository;
import com.spring.security.repository.TokenRepository;
import com.spring.security.response.RegisterResponse;
import com.spring.security.response.authentication.LoginResponse;
import com.spring.security.service.ServiceAuthentication;
import com.spring.security.service.ServiceJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceAuthenticationImpl implements ServiceAuthentication {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceJWT serviceJWT;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    @Override
    public ResponseEntity<LoginResponse> checkAccountByUsernameAndPassword(AuthenticationRequest request) {
        Optional<Account> account = accountRepository.getAccountByGmail(request.getGmail());
        if(account.isEmpty()){
            LoginResponse response = LoginResponse.builder()
                    .status("400")
                    .message("The account doesn't exists!")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getGmail(),
                        request.getPassword()
                )
        );
        String jwtToken = serviceJWT.generaToken(account.get());
        String refreshToken = serviceJWT.generateRefreshToken(account.get());
        revokedAllAccountToken(account.get().getId());
        saveToken(account.get().getId(), jwtToken);
        LoginResponse response = LoginResponse.builder()
                .status("200")
                .message("Verification successful.")
                .token(jwtToken)
                .refreshToken(refreshToken)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void revokedAllAccountToken(Long id) {
        try {
            tokenRepository.updateAllToken(id);
        }catch (Exception e){
            System.out.println("Error: "+ e);
        }
    }

    @Override
    public ResponseEntity<RegisterResponse> registerAccount(RegisterRequest request) {
        Optional<Account> accountByUsername = accountRepository.getAccountByUsername(request.getUsername());
        if(accountByUsername.isEmpty()){
            Optional<Account> accountByGmail = accountRepository.getAccountByGmail(request.getGmail());
            if(accountByGmail.isEmpty()){
                Account account = Account.builder()
                        .username(request.getUsername())
                        .gmail(request.getGmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .build();
                accountRepository.insertAccount(account);
                String jwtToken = serviceJWT.generaToken(account);
                String refreshToken = serviceJWT.generateRefreshToken(account);
                saveToken(account.getId(), jwtToken);
                RegisterResponse response = RegisterResponse.builder()
                        .status("200")
                        .message("Account registration successful.")
                        .token(jwtToken)
                        .refreshToken(refreshToken)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            RegisterResponse response = RegisterResponse.builder()
                    .status("400")
                    .message("The gmail account already exists.")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        RegisterResponse response = RegisterResponse.builder()
                .status("400")
                .message("The username already exists.")
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private void saveToken(long id, String jwtToken){
        Token token = Token.builder()
                .accountId(id)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        try{
            tokenRepository.insertToken(token);
        }catch(Exception e){
            System.out.println("Error:" + e);
        }
    }

}
