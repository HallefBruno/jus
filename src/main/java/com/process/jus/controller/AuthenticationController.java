package com.process.jus.controller;

import com.process.jus.security.dto.JwtAuthenticationDto;
import com.process.jus.security.dto.TokenDto;
import com.process.jus.security.entities.Response;
import com.process.jus.security.utils.JwtTokenUtil;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

  private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);
  private static final String TOKEN_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private UserDetailsService userDetailsService;

  /**
   * Gera e retorna um novo token JWT.
   *
   * @param authenticationDto
   * @param result
   * @return ResponseEntity<Response<>> @thr ows AuthenticationException
   */
  @PostMapping
  public ResponseEntity<Response<TokenDto>> gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDto authenticationDto, BindingResult result) throws AuthenticationException {
    Response<TokenDto> response = new Response<>();

    if (result.hasErrors()) {
      LOG.error("Erro validando lançamento: {}", result.getAllErrors());
      result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
      return ResponseEntity.badRequest().body(response);
    }

    LOG.info("Gerando token para o email {}.", authenticationDto.getEmail());
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getSenha()));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
    String token = jwtTokenUtil.obterToken(userDetails);
    response.setData(new TokenDto(token));

    return ResponseEntity.ok(response);
  }

  /**
   * Gera um novo token com uma nova data de expiração.
   *
   * @param request
   * @return ResponseEntity<Response<>>
   */
  @PostMapping(value = "/refresh")
  public ResponseEntity<Response<TokenDto>> gerarRefreshTokenJwt(HttpServletRequest request) {
    LOG.info("Gerando refresh token JWT.");
    Response<TokenDto> response = new Response<>();
    Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));

    if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
      token = Optional.of(token.get().substring(7));
    }

    if (!token.isPresent()) {
      response.getErros().add("Token não informado.");
    } else if (!jwtTokenUtil.tokenValido(token.get())) {
      response.getErros().add("Token inválido ou expirado.");
    }

    if (!response.getErros().isEmpty()) {
      return ResponseEntity.badRequest().body(response);
    }

    String refreshedToken = jwtTokenUtil.refreshToken(token.get());
    response.setData(new TokenDto(refreshedToken));

    return ResponseEntity.ok(response);
  }

}
