package domino.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Peca {
    private int numero1;
    private int numero2;

    public Peca(int numero1, int numero2) {
        this.numero1 = numero1;
        this.numero2 = numero2;
    }

    public int getNumero1() {
        return numero1;
    }

    public int getNumero2() {
        return numero2;
    }

    @Override
    public String toString() {
        return "[" + numero1 + "|" + numero2 + "]";
    }
}

class ListaDuplamenteEncadeada {
    private Nodo inicio;
    private Nodo fim;

    public void inserirInicio(Peca peca) {
        Nodo novo = new Nodo(peca);
        if (inicio == null) {
            inicio = novo;
            fim = novo;
        } else {
            novo.setProximo(inicio);
            inicio.setAnterior(novo);
            inicio = novo;
        }
    }

    public void inserirFim(Peca peca) {
        Nodo novo = new Nodo(peca);
        if (inicio == null) {
            inicio = novo;
            fim = novo;
        } else {
            novo.setAnterior(fim);
            fim.setProximo(novo);
            fim = novo;
        }
    }

    public Peca removerInicio() {
        if (inicio == null)
            return null;
        Peca removida = inicio.getPeca();
        if (inicio == fim) {
            inicio = null;
            fim = null;
        } else {
            inicio = inicio.getProximo();
            inicio.setAnterior(null);
        }
        return removida;
    }

    public Peca removerFim() {
        if (inicio == null)
            return null;
        Peca removida = fim.getPeca();
        if (inicio == fim) {
            inicio = null;
            fim = null;
        } else {
            fim = fim.getAnterior();
            fim.setProximo(null);
        }
        return removida;
    }

    public boolean estaVazia() {
        return inicio == null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Nodo atual = inicio;
        while (atual != null) {
            builder.append(atual.getPeca()).append(" ");
            atual = atual.getProximo();
        }
        return builder.toString();
    }

    public Peca removerPeca(int indice) {
        if (indice < 0 || indice >= tamanho() || estaVazia()) {
            return null;
        }

        Nodo atual = inicio;
        for (int i = 0; i < indice; i++) {
            atual = atual.getProximo();
        }

        if (atual == inicio && atual == fim) {
            inicio = null;
            fim = null;
        } else if (atual == inicio) {
            inicio = inicio.getProximo();
            inicio.setAnterior(null);
        } else if (atual == fim) {
            fim = fim.getAnterior();
            fim.setProximo(null);
        } else {
            atual.getAnterior().setProximo(atual.getProximo());
            atual.getProximo().setAnterior(atual.getAnterior());
        }

        return atual.getPeca();
    }

    public int tamanho() {
        int tamanho = 0;
        Nodo atual = inicio;
        while (atual != null) {
            tamanho++;
            atual = atual.getProximo();
        }
        return tamanho;
    }

    private class Nodo {
        private Peca peca;
        private Nodo anterior;
        private Nodo proximo;

        public Nodo(Peca peca) {
            this.peca = peca;
        }

        public Peca getPeca() {
            return peca;
        }

        public Nodo getAnterior() {
            return anterior;
        }

        public void setAnterior(Nodo anterior) {
            this.anterior = anterior;
        }

        public Nodo getProximo() {
            return proximo;
        }

        public void setProximo(Nodo proximo) {
            this.proximo = proximo;
        }
    }
}

class Output {
    public static void imprimirLista(String mensagem, ListaDuplamenteEncadeada lista) {
        System.out.println(mensagem + ": " + lista);
    }

    public static void imprimirVencedor(String vencedor) {
        System.out.println("O vencedor é: " + vencedor);
    }
}

class Input {
    public static int lerOpcao() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha uma opção:");
        System.out.println("1. Jogar uma peça");
        System.out.println("2. Passar a vez");
        return scanner.nextInt();
    }

    public static Peca escolherPeca(ListaDuplamenteEncadeada pecas) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Escolha o índice da peça que deseja jogar:");
        System.out.println(pecas);
        int indice = scanner.nextInt();


        if (indice < 0 || indice >= pecas.tamanho()) {
            System.out.println("Índice inválido. Por favor, escolha um índice válido.");
            return null;
        }


        return pecas.removerPeca(indice);
    }
}

class Menu {
    public static void main(String[] args) {

        ListaDuplamenteEncadeada pecasComputador = distribuirPecas();
        ListaDuplamenteEncadeada pecasHumano = distribuirPecas();
        ListaDuplamenteEncadeada pecasMesa = new ListaDuplamenteEncadeada();


        while (true) {

            Output.imprimirLista("Pecas na mesa", pecasMesa);


            if (pecasHumano.estaVazia()) {
                Output.imprimirVencedor("Computador");
                break;
            }


            if (pecasComputador.estaVazia()) {
                Output.imprimirVencedor("Humano");
                break;
            }


            int opcao = Input.lerOpcao();
            if (opcao == 1) {
                Peca pecaEscolhida = Input.escolherPeca(pecasHumano);
                pecasMesa.inserirFim(pecaEscolhida);
            }


            Peca pecaComputador = pecasComputador.removerInicio();
            pecasMesa.inserirFim(pecaComputador);


            Output.imprimirLista("Pecas do computador", pecasComputador);
            Output.imprimirLista("Pecas do humano", pecasHumano);
        }
    }

    private static ListaDuplamenteEncadeada distribuirPecas() {
        List<Peca> pecas = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                pecas.add(new Peca(i, j));
            }
        }
        Collections.shuffle(pecas);

        ListaDuplamenteEncadeada lista = new ListaDuplamenteEncadeada();
        for (Peca peca : pecas) {
            lista.inserirFim(peca);
        }
        return lista;
    }
}
