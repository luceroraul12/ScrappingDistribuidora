package distribuidora.scrapping.security.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtilService {
	private static final String JWT_SECRET_KEY = "EdLFRPG2BY65DEWWZ15atfVvuS4Zwh8stzZsk6jEwWQ=";

	public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * (long) 8; // 8
																				// Horas

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token,
			Function<Claims, T> claimsResolver) {
		return claimsResolver.apply(extractAllClaims(token));
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token)
				.getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(Map<String, String> mapExtraData) {
		Map<String, Object> claims = new HashMap<>();
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		// Agregando informacion adicional como "claim"
		var rol = auth.getAuthorities().stream().collect(Collectors.toList())
				.get(0);
		claims.put("rol", rol);
		claims.putAll(mapExtraData);
		return createToken(claims, auth.getName());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(
						System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY).compact();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())
				&& !isTokenExpired(token));
	}
}
