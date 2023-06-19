package com.purebank.accountservice.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.purebank.walletservice.wallet.WalletController;
import com.purebank.walletservice.wallet.resource.WalletResource;
import com.purebank.walletservice.wallet.service.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {
	@Autowired
	private MockMvc mvc;
	private ObjectMapper objectMapper = new ObjectMapper();
	private String API_WALLET = "/api/wallet";
	@MockBean
	private WalletService walletService;

	@BeforeEach
	void setup() {
		// Configuração inicial, se necessário
	}

	@Test
	void createWalletTest() throws Exception {
		WalletResource walletResource = new WalletResource();
		walletResource.setBalance(BigDecimal.valueOf(10));
		walletResource.setName("Test");
		walletResource.setId(1L);

		Mockito.doReturn(walletResource).when(walletService).createWallet(walletResource);

		String requestBody = "{ \"id\": 1, \"name\": \"Test\", \"balance\": 10 }";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(API_WALLET)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();
		WalletResource responseWallet = objectMapper.readValue(responseBody, WalletResource.class);

		Assertions.assertEquals(walletResource, responseWallet);
	}

	@Test
	void getWalletByIdTest() throws Exception {
		WalletResource walletResource = new WalletResource();
		walletResource.setBalance(BigDecimal.valueOf(10));
		walletResource.setName("Test");
		walletResource.setId(1L);

		Mockito.doReturn(walletResource).when(walletService).getWalletById(walletResource.getId());
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(API_WALLET + "/" + walletResource.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();
		WalletResource responseWallet = objectMapper.readValue(responseBody, WalletResource.class);

		Assertions.assertEquals(walletResource, responseWallet);
	}

	@Test
	void getBalanceTest() throws Exception {
		WalletResource walletResource = new WalletResource();
		walletResource.setBalance(BigDecimal.valueOf(10));
		walletResource.setName("Test");
		walletResource.setId(1L);

		Mockito.doReturn(walletResource.getBalance()).when(walletService).getBalance(walletResource.getId());
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(API_WALLET + "/" + walletResource.getId() + "/balance")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();
		BigDecimal responseBalance = objectMapper.readValue(responseBody, BigDecimal.class);

		Assertions.assertEquals(responseBalance, walletResource.getBalance());
	}

	@Test
	void updateWalletTest() throws Exception {
		WalletResource walletResource = new WalletResource();
		walletResource.setBalance(BigDecimal.valueOf(10));
		walletResource.setName("Test");
		walletResource.setId(1L);

		String requestBody = "{ \"id\": 1, \"name\": \"Test\", \"balance\": 10 }";
		WalletResource walletResourceUpdate = objectMapper.readValue(requestBody, WalletResource.class);
		walletResourceUpdate.setName("Test update");

		Mockito.doReturn(walletResourceUpdate).when(walletService).updateWallet(walletResource);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(API_WALLET)
						.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk())
				.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();
		WalletResource responseWallet = objectMapper.readValue(responseBody, WalletResource.class);

		Assertions.assertEquals(walletResourceUpdate, responseWallet);
	}
}
