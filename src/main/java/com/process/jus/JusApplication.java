package com.process.jus;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JusApplication {

//    @Autowired
//    private UsuarioRepository usuarioRepository;
  public static void main(String[] args) throws UnirestException {
    SpringApplication.run(JusApplication.class, args);
  }

//    @Bean
//    public CommandLineRunner commandLineRunner() {
//        return args -> {
//
//            Usuario usuario = new Usuario();
//            usuario.setEmail("usuario@email.com");
//            usuario.setPerfil(PerfilEnum.ROLE_USUARIO);
//            usuario.setSenha(SenhaUtils.gerarBCrypt("123456"));
//            this.usuarioRepository.save(usuario);
//
//            Usuario admin = new Usuario();
//            admin.setEmail("admin@email.com");
//            admin.setPerfil(PerfilEnum.ROLE_ADMIN);
//            admin.setSenha(SenhaUtils.gerarBCrypt("123456"));
//            this.usuarioRepository.save(admin);
//
//        };
//    }
//    Unirest.setTimeouts(0, 0);
//        HttpResponse<String> response = Unirest.post("https://kb-authorization-server.herokuapp.com/oauth/token")
//                .header("Authorization", "Basic bXlhcHBuYW1lMTIzOm15YXBwc2VjcmV0MTIz")
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .field("username", "nina@gmail.com")
//                .field("password", "123456")
//                .field("grant_type", "password")
//                .asString();
//        System.out.println(response.getBody());
}
