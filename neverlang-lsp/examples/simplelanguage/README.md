rm -rf simplelang.simplelang-nvim-client-0.0.1 && \
rm -rf simplelang.simplelang-vim-client-0.0.1 && \
rm -rf simplelang.simplelang-vscode-client-0.0.1 && \
gradle clean && \
gradle generateLSPClient
