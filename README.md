# 🚀 MyOwnHttpServer

Um projeto prático para desenvolver meu próprio servidor HTTP do zero, utilizando sockets, protocolos básicos da web e boas práticas de desenvolvimento. Ideal para entender como a comunicação HTTP funciona por trás dos navegadores e APIs modernas.

## 📌 Objetivo

Criar um servidor HTTP funcional que:
- Aceite conexões TCP via socket
- Interprete requisições HTTP (GET, HEAD)
- Sirva arquivos estáticos como HTML, CSS e imagens
- Retorne respostas com status apropriados (200, 404, 400, etc.)
- Suporte múltiplas conexões (concorrência com threads/processos)
- Faça logs de requisições
- Inclua testes, documentação e funcionalidades extras

## 🛠️ Tecnologias Utilizadas

- Linguagem: Java
- Sockets TCP
- Protocolo HTTP 1.1
- Threads ou multiprocessing para concorrência
- Scripts auxiliares (shell, batch)

## 🗂️ Estrutura de Pastas

```
/MyOwnHttpServer
├── /src          # Código-fonte do servidor
├── /public       # Arquivos estáticos servidos (html, css, etc)
├── /tests        # Testes unitários e manuais
├── /docs         # Documentação do projeto
├── /logs         # Arquivos de log
└── README.md     # Este arquivo
```

## 📄 Licença

Este projeto é open-source e licenciado sob a [MIT License](LICENSE).

---

> Desenvolvido com 💻 por [Pedro Magno](https://github.com/pedromagno11).  
> Projeto acadêmico para aprendizado de redes, sockets e servidores HTTP.