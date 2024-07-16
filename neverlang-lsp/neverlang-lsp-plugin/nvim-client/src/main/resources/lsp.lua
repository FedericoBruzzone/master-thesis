-- Put the contents of this file in your init.vim or init.lua file
-- Or source it from your init.vim or init.lua file

vim.api.nvim_create_autocmd('FileType', '{'
    pattern = "{0}",
    callback = function()
        local cmd = '{'
            "java",
            "-jar",
            "{1}/{0}-client.jar",
        '}'
        local client = vim.lsp.start('{'
            name = "{0}",
            cmd = cmd,
            root_dir = vim.fs.dirname(vim.fs.find('{' 'start.lsp', 'build.gradle' '}', '{' upward = true '}')[1]),
        '}')
        vim.lsp.buf_attach_client(0, client)
    end,
'}')
