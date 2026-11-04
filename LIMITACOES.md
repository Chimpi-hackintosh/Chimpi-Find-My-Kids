# Prévia experimental, NÃO é um sistema de segurança certificado

Este APK é uma compilação de **teste debug**, não substitui ligações telefônicas nem sistemas de emergência

- O app abre o SMS preenchido, mas o usuário precisa tocar ENVIAR
- Vários destinatários podem não funcionar no app de SMS escolhido; confira cada número
- O envio requer cobertura celular mesmo sem Wi-Fi, não há confirmação de entrega
- Sem rastreamento contínuo, sem pareamento ou acionamento automático da polícia
- Referências de delegacias são estáticas e não classificadas por proximidade
- Localização opcional pode estar indisponível ou desatualizada
- **Se houver risco imediato no Brasil, ligue 190**

## EventoSOS por Wi-Fi na prévia 1.1

- Só funciona com dois aparelhos na mesma LAN, IP correto, chave de pareamento correta e receptor ativo
- Não há servidor de nuvem, push notification pela internet, comunicação entre estados ou garantia de entrega
- Confirmar no app dos responsáveis significa **recebido pelo software**, não visto/ouvido por uma pessoa
- Com Wi-Fi desligado, celular desligado, roteador isolando clientes ou serviço interrompido, o aviso não chega
- A chave fica salva no armazenamento privado do aplicativo de testes; não há recuperação segura nem hardware-backed keystore nesta versão
- Envio SOS automático para polícia NÃO implementado
- Não dependa apenas deste software para emergências

## SMS por chip (prévia 1.2)

- A criança pode enviar SMS a até seis números de confiança cadastrados, somente depois de confirmação explícita na interface
- O SMS pode custar dinheiro e cada fragmento de uma mensagem longa pode ser cobrado separadamente
- A API de envio não certifica que a operadora entregou a mensagem nem que uma pessoa recebeu/visualizou
- Android com mais de um chip pode precisar de configuração do SIM padrão para SMS
- Sem cobertura da operadora, chip habilitado e envio internacional, não há comunicação com o Reino Unido
- O conteúdo enviado não é criptografado de ponta a ponta pela operadora, portanto evite detalhes sensíveis desnecessários
- O recurso exige permissão sensível SEND_SMS, sujeita a regras estritas da Google Play
