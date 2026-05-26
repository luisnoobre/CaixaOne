# CaixaOne — Sistema PDV Profissional

![Em desenvolvimento](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-green)
![React](https://img.shields.io/badge/React-18-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)

## 1. Visão Geral e a Dor

Pequenos comércios, mercadinhos, conveniências e distribuidoras enfrentam diariamente a dificuldade de controlar vendas, estoque e caixa sem pagar por sistemas caros e complexos. O **CaixaOne** resolve isso com um PDV (Ponto de Venda) moderno, intuitivo e acessível.

- **O que está sendo resolvido?** Controle de vendas, estoque, produtos, usuários e relatórios financeiros em um único sistema.
- **Quem sofre com esse problema?** Donos de pequenos comércios, operadores de caixa e gerentes que precisam de controle em tempo real.
- **Por que isso importa?** Um sistema PDV profissional reduz erros operacionais, evita perdas por falta de controle de estoque e acelera o atendimento ao cliente.

---

## 2. Arquitetura e Decisões Técnicas

| Camada | Escolha | Por que escolhi isso? | Alternativa considerada | Nota de impacto |
|---|---|---|---|---|
| Front-end | React + Tailwind CSS | Componentização, reatividade e estilização utilitária ágil | Vue.js | Alta produtividade, ecossistema maduro |
| Back-end | Java 21 + Spring Boot 3 | Tipagem forte, segurança robusta, ecossistema enterprise | Node.js | Confiabilidade, Spring Security integrado |
| Banco de dados | MySQL 8 | Relacional, transações ACID, amplamente suportado | PostgreSQL | Compatibilidade, facilidade de hospedagem |
| Autenticação | JWT + Spring Security | Stateless, escalável, sem necessidade de sessão no servidor | OAuth2 | Simplicidade de implementação e integração |
| API | REST | Padrão amplamente conhecido, fácil integração com frontend | GraphQL | Menor curva de aprendizado, manutenção simples |
| Pagamentos | PIX Copia e Cola (QR Code estático) | Integração nativa com bancos brasileiros via payload padrão BCB | API bancária paga | Gratuito, funciona com qualquer banco |

---

## 3. Funcionalidades Implementadas

- ✅ Autenticação JWT com login/cadastro/logout seguro
- ✅ Gestão de Produtos com desconto por porcentagem
- ✅ Sistema PDV com carrinho, múltiplas formas de pagamento
- ✅ Desconto automático para PIX e Dinheiro
- ✅ Geração de QR Code PIX válido (padrão BCB)
- ✅ Parcelamento no cartão de crédito com cálculo de juros
- ✅ Controle automático de estoque nas vendas
- ✅ Dashboard com métricas em tempo real
- ✅ Relatórios de vendas com exportação PDF e Excel
- ✅ Perfil de usuário com foto, dados da loja e chave PIX
- ✅ Isolamento de dados por usuário autenticado
- ✅ Splash Screen animada
- ✅ Tema claro profissional (Light Mode)

---

## 4. Destaque de Engenharia — Gerador PIX Copia e Cola

O trecho mais complexo do projeto foi a implementação do payload PIX seguindo o padrão EMV do Banco Central do Brasil, com cálculo de CRC16-CCITT para validação.

```js
const gerarPix = (chave, tipo, valor, nome, cidade) => {
  // Normaliza a chave conforme o tipo (telefone exige +55)
  let chaveFormatada = chave;
  if (tipo === 'telefone') chaveFormatada = `+55${chave.replace(/\D/g, '')}`;
  else if (tipo === 'cpf' || tipo === 'cnpj') chaveFormatada = chave.replace(/\D/g, '');
  else if (tipo === 'email') chaveFormatada = chave.toLowerCase().trim();

  // Monta campos no padrão TLV (Tag-Length-Value) do EMV
  const fmt = (id, val) => `${id}${String(val.length).padStart(2,'0')}${val}`;

  const semCRC = [
    fmt('00', '01'),
    fmt('26', fmt('00', 'BR.GOV.BCB.PIX') + fmt('01', chaveFormatada)),
    fmt('52', '0000'), fmt('53', '986'),
    fmt('54', Number(valor).toFixed(2)),
    fmt('58', 'BR'), fmt('59', nome.slice(0,25)),
    fmt('60', cidade.slice(0,15)),
    fmt('62', fmt('05', '***')), '6304'
  ].join('');

  // Calcula CRC16-CCITT para validação pelo banco receptor
  let crc = 0xFFFF;
  for (let i = 0; i < semCRC.length; i++) {
    crc ^= semCRC.charCodeAt(i) << 8;
    for (let j = 0; j < 8; j++)
      crc = (crc & 0x8000) ? (crc << 1) ^ 0x1021 : crc << 1;
  }
  return semCRC + (crc & 0xFFFF).toString(16).toUpperCase().padStart(4,'0');
};
```

> O payload gerado é lido por qualquer aplicativo bancário brasileiro, sem depender de APIs pagas.

---

## 5. Insights e Valor de Negócio

- **Para produto:** Reduz tempo de atendimento no caixa com PDV rápido e intuitivo
- **Para negócio:** Controle de estoque em tempo real evita perdas por ruptura ou excesso
- **Para financeiro:** Relatórios exportáveis em PDF e Excel facilitam a gestão do fluxo de caixa
- **Para escala:** Arquitetura preparada para evoluir para SaaS multi-tenant com isolamento por usuário já implementado

---

## 6. Instruções de Instalação e Uso

### Pré-requisitos
- Java 21
- Maven 3.9+
- MySQL 8.0
- Node.js 20+

### Backend

```bash
# 1. Clone o repositório
git clone https://github.com/luisnoobre/CaixaOne.git
cd CaixaOne/caixaone

# 2. Configure o banco de dados
# Crie o banco no MySQL:
# CREATE DATABASE caixaone;

# 3. Configure application.properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/caixaone
spring.datasource.username=root
spring.datasource.password=SUA_SENHA

# 4. Rode o backend
mvn spring-boot:run
# Acesse: http://localhost:8080
```

### Frontend

```bash
# Na pasta do frontend
cd CaixaOne/frontend

# Instale as dependências
npm install

# Rode o frontend
npm start
# Acesse: http://localhost:3000
```

### Primeiro acesso
- Acesse `http://localhost:3000`
- Crie uma conta em **"Criar Conta"**
- Configure sua **Chave PIX** e **Nome da Loja** em Configurações
- Cadastre produtos e comece a vender!

---

## 7. Roadmap — Próximos Passos

- [ ] Módulo de gerenciamento de usuários com controle de permissões (ADMIN/GERENTE/CAIXA)
- [ ] Gráficos no Dashboard (recharts) com evolução de vendas
- [ ] Cadastro de Categorias de Produtos
- [ ] Módulo de Clientes com histórico de compras
- [ ] Notificações de estoque baixo por email
- [ ] Deploy em produção (Railway + Vercel)
- [ ] Dockerização completa (docker-compose)
- [ ] Testes automatizados (JUnit + React Testing Library)
- [ ] Versão mobile responsiva

---

## 8. Tecnologias

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-6DB33F?logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?logo=springsecurity)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql)
![React](https://img.shields.io/badge/React-18-61DAFB?logo=react)
![Tailwind](https://img.shields.io/badge/Tailwind_CSS-3-38B2AC?logo=tailwindcss)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apachemaven)

---

> Desenvolvido por **Luis Henrique Nobre** — Estudante de Ciência da Computaçãoisões técnicas claras.
- O objetivo é mostrar que você sabe resolver problemas reais e justificar suas escolhas.
