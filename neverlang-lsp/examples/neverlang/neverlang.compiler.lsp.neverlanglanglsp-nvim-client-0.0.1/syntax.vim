" Put this file in ~/.config/nvim/syntax/ in order to enable syntax highlighting
if exists("b:current_syntax")
  finish
endif

syntax clear

" Keywords
syn keyword neverlang.compiler.lsp.neverlanglanglspControl endemic slice init declare static tags bundle categories with style in-buckets out-buckets language slice slices bundle slices endemic roles syntax rename slice slices bundle module imports module imports role reference syntax mapping slice syntax concrete from module with as mapping mapping attributes tags provides requires role syntax left

" Operators
syn keyword neverlang.compiler.lsp.neverlanglanglspOperator : ; . , ; : = , ; : <-- --> $ # + --> * . * ; * : < > ! ; * : < > ! ; ; <-- < , , = > , = > : : : , ; : [ ] [ ]

" Constants
syn keyword neverlang.compiler.lsp.neverlanglanglspConstant 

" Comments
syn match neverlang.compiler.lsp.neverlanglanglspComment ".*$"
syn region neverlang.compiler.lsp.neverlanglanglspComment start="" end=""

" String
syn match neverlang.compiler.lsp.neverlanglanglspString /".*"/

" Highlighting
hi def link neverlang.compiler.lsp.neverlanglanglspControl Keyword
hi def link neverlang.compiler.lsp.neverlanglanglspOperator Operator
hi def link neverlang.compiler.lsp.neverlanglanglspConstant Constant
hi def link neverlang.compiler.lsp.neverlanglanglspComment Comment
hi def link neverlang.compiler.lsp.neverlanglanglspString String

let b:current_syntax = "neverlang.compiler.lsp.neverlanglanglsp"

