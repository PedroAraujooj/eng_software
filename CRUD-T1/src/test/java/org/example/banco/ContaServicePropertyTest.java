package org.example.banco;

import net.jqwik.api.*;
import net.jqwik.api.lifecycle.BeforeTry;
import org.example.banco.entity.Conta;
import org.example.banco.repository.ContaRepository;
import org.example.banco.service.ContaService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContaServicePropertyTest {
    private ContaRepository contaRepository;
    private ContaService contaService;

    @BeforeTry
    void setup() {
        contaRepository = mock(ContaRepository.class);
        contaService = new ContaService(contaRepository);
    }

    @Provide
    Arbitrary<BigDecimal> saldosMonetarios() {
        return Arbitraries.bigDecimals()
                .between(new BigDecimal("0.01"), new BigDecimal("1000000.00"))
                .map(bd -> bd.setScale(2, RoundingMode.HALF_UP));
    }

    @Provide
    Arbitrary<BigDecimal> valoresMonetarios() {
        return Arbitraries.bigDecimals()
                .between(new BigDecimal("0.01"), new BigDecimal("1000000.00"))
                .map(bd -> bd.setScale(2, RoundingMode.HALF_UP));
    }


    @Property
    void depositarDeveSomarAoSaldo(@ForAll("saldosMonetarios") BigDecimal saldoInicial,
                                   @ForAll("valoresMonetarios") BigDecimal valorDeposito) {
        Conta conta = new Conta(1L, "Teste", saldoInicial);

        conta.depositar(valorDeposito);

        BigDecimal esperado = saldoInicial.add(valorDeposito);
        assertThat(conta.getSaldo())
                .isEqualByComparingTo(esperado);
    }

    @Property
    void debitarDeveDiminuirSaldo(@ForAll("saldosMonetarios") BigDecimal saldoInicial,
                                  @ForAll("valoresMonetarios") BigDecimal valorDebito) {
        Assume.that(valorDebito.compareTo(saldoInicial) <= 0);

        Conta conta = new Conta(1L, "Teste", saldoInicial);

        conta.debitar(valorDebito);

        BigDecimal esperado = saldoInicial.subtract(valorDebito);
        assertThat(conta.getSaldo())
                .isEqualByComparingTo(esperado);
    }

    @Property
    void debitarMaiorQueSaldoDeveLancarExcecao(@ForAll("saldosMonetarios") BigDecimal saldoInicial,
                                               @ForAll("valoresMonetarios") BigDecimal valorDebito) {
        Assume.that(valorDebito.compareTo(saldoInicial) > 0);

        Conta conta = new Conta(1L, "Teste", saldoInicial);

        assertThatThrownBy(() -> conta.debitar(valorDebito))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Property
    void transferirDeveDebitarOrigemECreditarDestino(@ForAll("saldosMonetarios") BigDecimal saldoOrigem,
                                                     @ForAll("saldosMonetarios") BigDecimal saldoDestino,
                                                     @ForAll("valoresMonetarios") BigDecimal valorTransferencia) throws Exception {
        Assume.that(valorTransferencia.compareTo(saldoOrigem) <= 0);

        Conta origem = new Conta(1L, "Origem", saldoOrigem);
        Conta destino = new Conta(2L, "Destino", saldoDestino);

        when(contaRepository.findById(1L)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(destino));
        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal totalAntes = saldoOrigem.add(saldoDestino);

        contaService.transferir(1L, 2L, valorTransferencia);

        BigDecimal saldoOrigemEsperado = saldoOrigem.subtract(valorTransferencia);
        BigDecimal saldoDestinoEsperado = saldoDestino.add(valorTransferencia);

        assertThat(origem.getSaldo()).isEqualByComparingTo(saldoOrigemEsperado);
        assertThat(destino.getSaldo()).isEqualByComparingTo(saldoDestinoEsperado);

        assertThat(origem.getSaldo().add(destino.getSaldo()))
                .isEqualByComparingTo(totalAntes);
    }
}
