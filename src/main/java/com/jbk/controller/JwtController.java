package com.jbk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jbk.helper.JwtUtil;
import com.jbk.model.JwtRequest;
import com.jbk.model.JwtResponse;
import com.jbk.service.CustomUserDetailsService;

@RestController
public class JwtController 
{
	    @Autowired
	    private AuthenticationManager authenticationManager;


	    @Autowired
	    private CustomUserDetailsService customUserDetailsService;

	    @Autowired
	    private JwtUtil jwtUtil;
	
	@RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		System.out.println("Inside Controller");
        System.out.println(jwtRequest);
        try 
        {
        	 // Attempt to authenticate the user's credentials using AuthenticationManager
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));

        }
        catch (UsernameNotFoundException | BadCredentialsException e)
        {
        	// Handle exceptions for invalid credentials
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }
        
        // Retrieve UserDetails for the authenticated user
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

         // Generate a JWT token for the authenticated user
        String token = this.jwtUtil.generateToken(userDetails);
        System.out.println("JWT " + token);

        //{"token":"value"}

        // Return the JWT token in a ResponseEntity
        return ResponseEntity.ok(new JwtResponse(token));
		
	}

}
