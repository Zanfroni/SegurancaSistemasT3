# SegurancaSistemasT3
Terceiro trabalho da disciplina de Segurança de Sistemas (Engenharia de Software, PUCRS)

INSTRUÇÕES DE USO:

Deve-se entrar no mesmo diretório que o app.jar está localizado.

Usar o seguinte comando para executar o código: *java -jar app.jar*

- Digite o número de bits que desejas que os números primos e chaves sejam gerados.

- Assim que gerados, digite a mensagem. O teste padrão que utilizei foi tamanho 1024 bits e mensagem "bom dia avelino zorzo!"

- O código faz o resto e imprime tudo de importante na tela!

Nota-se que existe um ITERATION_LIMIT que pode ser ajustado. Ele apenas delimite quantas iterações o gerador de números pseudo-aleatórios pode fazer até o sistema parar ele, evitando um loop infinito. O padrão definido foi 12000 iterações. De 1024 a 2048, está de ótimo tamanho mas pode-se testar com valores maiores. O valor ONE é apenas uma constante usada nas fórmulas recorrentes no código. Definiu-se uma constante para evitar de declarar *new BigInteger("1")* toda vez.

O código segue fielmente o que foi requisitado no enunciado, com todas as impressões feitas (mais algumas bônus como iterações), a verificação de Fermat implementada a mão e bibliotecas de auxílio (cortesia do BigInteger) utilizadas para a parte de geração de chaves (como gdc(), modPow() e modInverse()).

Comentários no código auxiliam em entender como segue sua lógica, referenciando as fórmulas aprendidas e usadas em aula para calcuilo de aritmética modular.

Inicialmente foi implementado em Python mas foi abandonado devido à impossibilidade de gerar chaves de mais de 1000 bits em tempo aceitável.
