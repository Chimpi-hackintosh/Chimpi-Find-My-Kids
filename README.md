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

## 📱 EventoSOS via chip/SMS (v1.2.0-preview.1)

1. Cadastre sua tia (+55...) e seus pais no Reino Unido (+44...), com DDI completo
2. Toque **SOS VIA CHIP PARA TODOS OS FAMILIARES** e confirme os destinatários e custos
3. Conceda permissão **SEND_SMS** quando o Android solicitar
4. Cada SMS é enviado individualmente por meio da rede celular; não exige dados móveis nem Wi-Fi, mas exige sinal, chip/eSIM ativo e cobertura internacional
5. No Chimpi Parents, os familiares poderão autorizar manualmente o número da criança e ativar o aviso por SMS

Esta função **não garante entrega**, não confirma que algum adulto leu e não aciona a polícia. SMS internacionais e multipartes podem ser cobrados pela operadora. Se o SMS não funcionar, ligue para um adulto ou 190 em emergências no Brasil

**Compatibilidade**: apps Android com permissões diretas de SMS sofrem restrições de distribuição no Google Play e precisam de revisão antes de lançamento público na loja

## 🚨 Quem recebe o quê? v1.3.0-preview.1

- **Tia / cuidador local**: recebe `CHIMPI-SOS` com o relato e a localização opcional
- **Mãe e pai**: recebem somente `CHIMPI-STATUS | AGUARDANDO`, com o ID do episódio e o aviso de que um familiar foi acionado, mas **ainda não confirmou que está ajudando**
- A tia usa o Chimpi Parents em modo **Cuidador** e toca em **Estou ajudando** para mandar uma atualização `CHIMPI-STATUS | CONFIRMADO` aos números dos pais que ela cadastrou
- O app não verifica entrega da operadora e não deve afirmar que a tia está ajudando sem confirmação humana
- Para garantir uma alternativa em emergências, familiares devem conversar diretamente; o Chimpi não substitui serviços de emergência

A preferência por status resumido para os pais não oculta a existência de uma emergência: eles recebem o aviso de ajuda pendente. SMS internacional depende da operadora e pode falhar

### Ajuste 1.3.1

O botão SMS manual prepara a mensagem completa **somente para o cuidador local**, sem incluir pai ou mãe. Para avisos de andamento aos pais, use **SOS PARA TIA + STATUS AOS PAIS**. Se o pedido de SMS para o cuidador falhar ao ser entregue ao Android, os pais receberão um status de falha, e não uma falsa confirmação de acionamento. A operadora ainda pode falhar após o envio inicial


## Aviso de suporte

O aplicativo compara a versão instalada com a prévia publicada v1.3.1-preview.1 e detecta o idioma do Android, usando português ou inglês como alternativa

Após 04/11/2026 às 00h UTC, versões anteriores informam que não recebem mais atualizações de segurança e apontam somente para a Release oficial

Versões antigas sem o verificador precisam de uma atualização manual inicial

Código neste PR não é um APK novo, não atualiza instalações existentes e não publica Release
