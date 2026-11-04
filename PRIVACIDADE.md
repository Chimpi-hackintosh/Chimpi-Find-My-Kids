# Privacidade | Chimpi Find My Kids 1.1.0-preview.1

Esta versão experimental não usa servidor próprio, anúncios, telemetria nem coleta remota de dados

- Os contatos e o pareamento (IP e chave) são salvos **localmente** nas preferências privadas do aplicativo Android
- Quando a criança seleciona **Enviar EventoSOS por Wi-Fi** ou confirma o gesto SOS, o aparelho envia a mensagem diretamente ao celular pareado na mesma rede local
- A mensagem e a localização opcional são protegidas em trânsito por AES-256-GCM com chave previamente compartilhada
- A localização só é incluída se a pessoa marcar a opção e conceder a permissão; dados podem estar indisponíveis ou desatualizados
- Qualquer pessoa com a chave de pareamento e acesso à rede poderá tentar enviar mensagens; não compartilhe essa chave publicamente
- **Não há identificação verificada**, armazenamento de chave em Android Keystore, supervisão profissional, garantia de entrega ou comunicação com autoridades
- O usuário deve poder evitar o envio e editar o texto; o adulto deve aprovar previamente o pareamento

Para instalação com crianças e adolescentes, obtenha consentimento adequado do responsável legal, limite acesso à chave e teste o sistema antes de uso

Este protótipo não deve ser o único meio de pedir socorro
