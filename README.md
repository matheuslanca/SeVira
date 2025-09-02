# 🚇 SeVira

<p align="center">
  <img src="https://img.shields.io/badge/Android%20Studio-3DDC84?logo=android-studio&logoColor=white" alt="Android Studio">
  <img src="https://img.shields.io/badge/Firebase-FFCA28?logo=firebase&logoColor=black" alt="Firebase">
  <img src="https://img.shields.io/badge/License-MIT-green" alt="License">
</p>

**SeVira** é um aplicativo Android desenvolvido em **Java (Android Studio)** com backend no **Firebase**, idealizado para que usuários do metrô denunciem de forma rápida e colaborativa pequenos problemas — como escadas rolantes quebradas, ar-condicionado desligado ou superlotação — permitindo que futuras gerações de passageiros fiquem informadas em tempo real.

---

## ✨ Funcionalidades principais

- 🆘 **Denúncias em tempo real** sobre problemas no metrô  
- 🔔 **Notificações locais ou push** para alertar outros passageiros  
- 📰 **Feed organizado** com denúncias recentes e relevantes  
- 👤 **Autenticação de usuários** (Firebase Auth)  
- ☁️ **Armazenamento na nuvem** (Firebase Realtime Database ou Firestore)  

---

## 🛠️ Tecnologias usadas

- **Java** (linguagem principal do app)  
- **Android Studio** (IDE de desenvolvimento)  
- **Firebase** — Auth, Realtime Database / Firestore, Storage, Cloud Messaging  
- **Material Design** para interface moderna  

---

## 📂 Estrutura do projeto

```text
/app
  └─ src
     └─ main
        ├─ java/com/sevira
        │    ├─ activities       // telas do app
        │    ├─ adapters         // adaptadores (RecyclerView, etc.)
        │    ├─ models           // classes de dados (ex: Denuncia, Usuario)
        │    └─ services         // comunicação com Firebase
        └─ res
             ├─ layout           // arquivos XML de layout
             └─ values           // cores, strings, estilos, dimensões

```
⚙️ Como executar o app

Clone o repositório:

git clone https://github.com/matheuslanca/SeVira.git

Abra o projeto no Android Studio

Configure o Firebase:

Crie um projeto no Firebase Console

Adicione seu app Android (usando o package name do projeto)

Baixe o arquivo google-services.json e coloque em /app

Ative os serviços necessários (Auth, Database, Storage etc.)

Sincronize o Gradle

Execute no emulador ou dispositivo físico (Run ▶)

📄 Licença

Este projeto está licenciado sob a MIT License — veja o arquivo LICENSE
.

