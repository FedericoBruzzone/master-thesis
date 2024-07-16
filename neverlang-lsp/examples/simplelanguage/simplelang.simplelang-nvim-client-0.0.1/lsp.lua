-- Put the contents of this file in your init.vim or init.lua file
-- Or source it from your init.vim or init.lua file

vim.api.nvim_create_autocmd(FileType, {
    pattern = "simplelang.SimpleLang",
    callback = function()
        local cmd = {
            "java",
            "-jar",
            "/Users/federicobruzzone/Documents/ADAPT-LAB/tesi-dagostino/trunk/examples/simplelanguage/build/libs/simplelang.SimpleLang-client.jar",
        }
        local client = vim.lsp.start({
            name = "simplelang.SimpleLang",
            cmd = cmd,
            root_dir = vim.fs.dirname(vim.fs.find({ start.lsp, build.gradle }, { upward = true })[1]),
        })
        vim.lsp.buf_attach_client(0, client)
    end,
})
