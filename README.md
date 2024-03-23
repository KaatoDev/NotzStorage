<div align="center">
<img src="https://github.com/KaatoDev/NotzStorage/assets/107152563/c1ee9490-47b8-4b7d-856a-024430cc5b1d" alt="" height="320" >

#
NotzStorage é um plugin completo de `Armazém`, `Farm Virtual`, `Boosters` e "`Bolsa de valores`".

###### (Plugin em desenvolvimento)

## Informações
### `Armazém`
Ele conta com um sistema de armazém de plantação intuitivo que coleta automaticamente as plantações (com permissão: todas, sem permissão: cacto e cana-de-açúcar) e sem deixá-las crescerem, como também replanta ao serem colhidas.
### `Farm Virtual`
Intitulado como "Virtual plot", ele simula uma farm completa de cacto como se fosse de uma plot 32x32x255, farmando 3840 por minuto quando cheia e com limite de armazém de 2.8KK. (Será possível aumentar a velocidade/produção e o limite de storage em breve! Como também contará com mais variedades de plantação)
### `Boosters`
É possível criar Boosters personalizados e, por padrão, setar a sua porcentagem. O tempo é adicionado ao criar um item ativável. (Logo mais será possível setar o Booster diretamente caso seja necessário, como para ganhadores de eventos etc)
### `Bolsa de valores`
A "Bolsa de Valores" é uma simulação de alta e baixa da própria economia que roda em tordo do plugin, ela altera automáticamente pelo próprio tempo definido cada vez que atualiza.
### Outros
É possível também desabilitar as notificações gerais, tanto de informativo de farm quanto de venda.

</div>

## Dependências
- PlotSquared
- Vault

## Spoilers
- ### Armazém

![image](https://github.com/KaatoDev/NotzStorage/assets/107152563/591d752a-09e8-4f63-9654-b4dcf8d00105)
![image](https://github.com/KaatoDev/NotzStorage/assets/107152563/b025c9be-00dd-4fb8-9092-ceb3977f5e15)

- ### Farm Virtual
![image](https://github.com/KaatoDev/NotzStorage/assets/107152563/ce2858cb-9676-4809-b57a-14749a540f8e)

- ### Boosters
 ![image](https://github.com/KaatoDev/NotzStorage/assets/107152563/e4109ce2-458f-43bd-bce1-572772c7b4b8)
 ![image](https://github.com/KaatoDev/NotzStorage/assets/107152563/91344065-dad0-4592-9498-85da78bc85ff)


- ### Bolsa de Valores
![image](https://github.com/KaatoDev/NotzStorage/assets/107152563/0fb6a7b9-686d-441d-bac3-f64ff25f97fa)

## Permissões

- `notzstorage.autofarm` - Habilita a autofarm das plantações além do cacto e cana-de-açúcar.
- `notzstorage.admin` - Possibilita o uso dos comandos de admin.

## Commandos `Players`
 - `/armazem` - Abre o menu de armazém e configuração de notificação
 - `/bolsa` - Chega a porcentagem da bolsa atual.
 - `/boosters` - Checa seus Boosters ativos e o tempo restante de cada um.
 - `/farms` - Visualiza o total de plantação em suas Farms Virtuais e Plots. (De plots será adicionado futuramente!)
 - `/prices` - Checa o preço unitário de venda de cada plantação.
 - `/sell` - Vende apenas as plantações armazenadas.
 - `/sellall` - Vende as plantações armazenadas e do inventário.
 - `/virtualplot` -  Abre o menu de Virtual Plots.

## Comandos `Admin`
 ### `/nbooster`
 - `create` - \<nome> \<display> \<%> - Cria um novo Booster.
 - `list` - Lista os Boosters existentes.
 - `remove` \<booster> - Exclui um Booster.
 - `<booster>`
   - `get` \<tempo em segundos> (player) - Pegue ou dê o Booster.
   - `setDisplay` \<novo display> - Altera o display do Booster.
   - `percentage` \<nova %> - Altera a porcentagem do Booster.

 ### `/nvirtualplot`
  - `backup` - Faz backup da database.
  - `addvp` \<Player> - Adiciona uma Virtual Plot à um player.
  - `removevp` \<Player> \<Plot id> - Exclui uma Virtual Plot.
  - `setcactus` \<Player> \<Id da VP> \<32/64> - Adiciona 32K ou 64K à Virtual Plot de um player.
  - `setprice` 
    - `<item>` \<price> - Seta o valor de um item de farm.
    - `cactusVP` \<price> - Seta o valor do item cacto da Virtual Plot
  - `updatestock` - Atualiza a bolsa de valores.

### `Aliases`
- `/virtualplot`:
  - `/plotvirtual`, `/vp`
- `/bolsa`
  - `/stock`
- `/booster`
  - `/boost`, `/boosts`, `/boosters`
- `/armazem`
  - `/storage`, `/drop`, `/drops`
- `/farms`
  - `/farm`
- `/nbooster`
  - `/nb`, `/nboost`, `/nboosts`, `/nboosters`
- `/nvirtualplot`:
  - `/nvp`


 ###### | <> argumento obrigatório. | () argumento opcional. |
 
#
###### Versões testadas: 1.8
