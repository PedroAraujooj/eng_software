package org.example.banco;

import lombok.RequiredArgsConstructor;
import org.example.banco.entity.Conta;
import org.example.banco.service.ContaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class CrudT1Application implements CommandLineRunner {

    private final ContaService contaService;


    public static void main(String[] args) {
        SpringApplication.run(CrudT1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        consultarContas();
        incluirConta("Pedro", new BigDecimal("0.0"));
        consultarContas();
        depositar(1L, new BigDecimal("50.0"));
        debitar(1L, new BigDecimal("25.0"));
        transferir(1L, 2L, new BigDecimal("15.00"));
        consultarConta(1L);
        excluirConta(1L);
        consultarContas();
    }

    public void depositar(Long id, BigDecimal saldo) throws Exception {
        contaService.depositar(id, saldo);
        System.out.println("Deposito executado.");
    }

    public void debitar(Long id, BigDecimal saldo) throws Exception {
        contaService.debitar(id, saldo);
        System.out.println("Debito executado.");
    }

    public void transferir(Long origem, Long destino,  BigDecimal valor) throws Exception {
        contaService.transferir(origem, destino, valor);
        System.out.println("Transferencia executada.");
    }

    public void consultarConta(Long id) throws Exception {
        Conta conta = contaService.consultarContaDb(id);
        System.out.println(conta);
    }

    public void excluirConta(Long id) {
        contaService.excluirContaDb(id);
        System.out.println("Conta de id " + id + " excluida");
    }
    public void incluirConta(String nome, BigDecimal saldo) {
        contaService.incluirContaDb(nome, saldo);
        System.out.println("Conta incluida");

    }

    public void consultarContas() {
        List<Conta> contas = contaService.consultarContasDb();
        for (Conta conta : contas) {
            System.out.println(conta);
        }
    }
}
