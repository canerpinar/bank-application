package com.oredata.bank;

import com.oredata.bank.dto.AccountDTO;
import com.oredata.bank.dto.UserDTO;
import com.oredata.bank.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class BankBackEndApplication implements CommandLineRunner {


	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(BankBackEndApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		UserDTO userDTO = new UserDTO("tantan","1234","tantan@gmail.com",
				new HashSet<>(List.of(new AccountDTO("12-345","Tantan hesap1","kemal ileri", BigDecimal.valueOf(10000)),new AccountDTO("12-3456","Tantan Hesap2","ali su", BigDecimal.ZERO))));



		UserDTO userDTO1 = new UserDTO("furkan","1234","furk@gmail.com",
				new HashSet<>(List.of(new AccountDTO("12-678","Furkan Hesap1","kemal ileri", BigDecimal.ZERO),new AccountDTO("12-6789","Furkan Hesap 2","ali su", BigDecimal.ZERO))));


		UserDTO userDTO2 = new UserDTO("alican","1234","ali@gmail.com",
				new HashSet<>(List.of(new AccountDTO("222222","Ali Can","alican", BigDecimal.ZERO),new AccountDTO("3333","Ali Hesap 2","ali su", BigDecimal.ZERO))));



		userService.save(userDTO);
		userService.save(userDTO1);
		userService.save(userDTO2);

	}

}
