package com.textbasedgame.auth;

import java.util.Date;

public record JwtTokenPayload(String id, String email, String username, Date expirationTime) {
}
