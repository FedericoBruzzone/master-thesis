-- Put the contents of this file in your init.vim or init.lua file
-- Or source it from your init.vim or init.lua file

vim.api.nvim_create_autocmd(FileType, {
    pattern = "neverlang.compiler.lsp.NeverlangLangLSP",
    callback = function()
        local cmd = {
            "java",
            "-jar",
            "/home/fcb/Documents/neverlang-lsp/examples/neverlang/build/libs/neverlang.compiler.lsp.NeverlangLangLSP-client.jar",
        }
        local client = vim.lsp.start({
            name = "neverlang.compiler.lsp.NeverlangLangLSP",
            cmd = cmd,
            root_dir = vim.fs.dirname(vim.fs.find({ start.lsp, build.gradle }, { upward = true })[1]),
        })
        vim.lsp.buf_attach_client(0, client)
    end,
})
