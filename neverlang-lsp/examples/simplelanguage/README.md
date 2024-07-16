rm -rf simplelang-nvim-client-0.0.1 && \
rm -rf simplelang-vim-client-0.0.1 && \
rm -rf simplelang-vscode-client-0.0.1 && \
gradle clean && \
gradle generateLSPClient
