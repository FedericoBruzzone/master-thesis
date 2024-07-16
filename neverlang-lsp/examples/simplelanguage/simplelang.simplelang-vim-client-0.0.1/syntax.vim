" Put this file in ~/.config/nvim/syntax/ in order to enable syntax highlighting
if exists("b:current_syntax")
  finish
endif

syntax clear

" Keywords
syn keyword simplelang.simplelangControl function return if else

" Operators
syn keyword simplelang.simplelangOperator + - * /

" Constants
syn keyword simplelang.simplelangConstant true false

" Comments
syn match simplelang.simplelangComment "//.*$"
syn region simplelang.simplelangComment start="/\\*" end="\\*/"

" String
syn match simplelang.simplelangString /".*"/

" Highlighting
hi def link simplelang.simplelangControl Keyword
hi def link simplelang.simplelangOperator Operator
hi def link simplelang.simplelangConstant Constant
hi def link simplelang.simplelangComment Comment
hi def link simplelang.simplelangString String

let b:current_syntax = "simplelang.simplelang"

