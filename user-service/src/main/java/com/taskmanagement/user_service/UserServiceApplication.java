package com.taskmanagement.user_service;

import com.taskmanagement.user_service.DTO.JwtResponse;
import com.taskmanagement.user_service.DTO.LoginRequest;
import com.taskmanagement.user_service.config.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@RestController
	public class HealthController {
		@GetMapping("/api/auth/health")
		public String health() {
			return "Service is healthy!";
		}
	}

	@RestController
	@RequestMapping("/api/auth")
	public class AuthController {

		private final AuthenticationManager authenticationManager;
		private final JwtUtils jwtUtils;

		public AuthController(AuthenticationManager authenticationManager,
							  JwtUtils jwtUtils) {
			this.authenticationManager = authenticationManager;
			this.jwtUtils = jwtUtils;
		}

		@PostMapping("/api/auth/login")
		public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
			try {
				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(
								loginRequest.getUsername(),
								loginRequest.getPassword()
						)
				);

				SecurityContextHolder.getContext().setAuthentication(authentication);
				String jwt = jwtUtils.generateJwtToken(authentication);

				return ResponseEntity.ok(new JwtResponse(jwt));

			} catch (BadCredentialsException e) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
			}
		}
	}
}
