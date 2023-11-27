# Engenharia de Dados com Scala: aprenda a fazer webscraping dos top 10 filmes mais assistidos da Netflix em cada país

Esse projeto também está disponível em formato de artigo no [Dev Community](https://dev.to/geazi_anc/engenharia-de-dados-com-scala-aprenda-a-fazer-webscraping-dos-filmes-mais-assistidos-da-netflix-em-cada-pais-582h#1-an%C3%A1lise-de-requisitos) 😉.

## 💻 Sobre o projeto

No vasto universo do streaming, a Netflix reina soberana em seu trono, proporcionando entretenimento a milhões de espectadores em todo o globo. Enquanto os usuários mergulham nos extensos catálogos de filmes, séries e documentários, uma pergunta intrigante surge: quais são os filmes mais assistidos em diferentes partes do mundo? Em busca dessa resposta, embarcamos em uma jornada de engenharia de dados, utilizando a poderosa linguagem Scala para extrair e analisar os top 10 filmes mais assistidos de cada país na plataforma.

A essência deste projeto reside na aplicação de webscraping para desvendar os segredos guardados nas páginas da Netflix, revelando não apenas os filmes mais populares, mas também proporcionando insights valiosos sobre as nuances culturais que moldam o gosto cinematográfico em diferentes nacionalidades.

Para isso, desenvolveremos uma pipeline de dados com a linguagem Scala para a extração, transformação e carregamento desses dados, com todo o desenvolvimento baseado no paradigma da programação funcional. Além disso, como era de se imaginar, iremos utilizar algumas bibliotecas não nativas da linguagem durante esse processo, tais como STTP para solicitações HTTP, uPickle para a leitura e criação de objetos json e Scala XML para a manipulação de conteúdos XML.

Portanto, se você se interessou em saber sobre os top 10 filmes mais assistidos na Netflix em cada país ao redor do mundo, junte-se a mim nessa emocionante jornada de exploração de dados!

## 🛠 Tecnologias

Para esse projeto foi utilizado a linguagem Scala em conjunto com as seguintes bibliotecas:

- STTP para solicitações HTTP;
- uPickle para serialização de JSON;
- Scala XML para a manipulação de dados XML;
- Scribe para logging da pipeline;
