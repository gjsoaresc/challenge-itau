package com.itau.seguros;

import org.springframework.boot.SpringApplication;

public class TestSegurosApplication {

	public static void main(String[] args) {
		SpringApplication.from(SegurosApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
