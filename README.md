# 🐒 Chimpi Find My Kids

**Prévia experimental do app infantil Android**

APK de testes em [Releases](../../releases) após compilação automática

Funções: mensagem livre, 3 níveis de SOS, até 6 contatos familiares, SMS preparado manualmente, localização opcional, contagem cancelável e referências de delegacias

⚠️ Nenhum SMS é enviado automaticamente, nenhum pedido à polícia é registrado, nenhum alerta é entregue remotamente por este app sem o envio manual da mensagem

Em emergência, ligue **190**

`gradle :app:assembleDebug` (Java 17, Gradle 8.9, SDK 35)

## 📶 EventoSOS Wi-Fi v1.1.0-preview.1

O aplicativo pode agora **enviar** EventoSOS autenticado e criptografado por Wi-Fi ao app dos responsáveis na mesma LAN

1. Instale e abra **Chimpi Parents Find My Kids** no celular de um responsável
2. O adulto ativa a escuta local com notificação permanente e compartilha **IP e chave de pareamento** com a criança
3. No Chimpi Find My Kids, insira o IP e a chave, pressione **Testar conexão** para receber uma confirmação de teste (sem alarme)
4. Toque **ENVIAR EVENTOSOS POR WI-FI** ou segure e solte o botão SOS para iniciar contagem cancelável de 8 segundos
5. Ao receber o aviso, o responsável recebe uma notificação, vibração, alarme e fala “ALERTA! SEU FILHO OU FILHA ESTÁ EM APUROS!” se a síntese de voz estiver disponível

A mensagem usa **AES-256-GCM** e o receptor confirma recebimento pela rede; isso **não garante** que um adulto viu ou ouviu. Requer **ambos na mesma rede local Wi-Fi** com o serviço dos responsáveis ativo. Sem internet pode funcionar somente dentro desta mesma rede, mas não funciona com parentes em outro estado ou outra rede Wi-Fi. Em perigo imediato, ligue 190 ou use outras formas confiáveis de contato.
