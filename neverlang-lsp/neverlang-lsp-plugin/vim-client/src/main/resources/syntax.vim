" Put this file in ~/.config/nvim/syntax/ in order to enable syntax highlighting
if exists("b:current_syntax")
  finish
endif

syntax clear

" Keywords
syn keyword {0}Control {1}

" Operators
syn keyword {0}Operator {2}

" Constants
syn keyword {0}Constant {3}

" Comments
syn match {0}Comment "{4}.*$"
syn region {0}Comment start="{5}" end="{6}"

" String
syn match {0}String {7}

" Highlighting
hi def link {0}Control Keyword
hi def link {0}Operator Operator
hi def link {0}Constant Constant
hi def link {0}Comment Comment
hi def link {0}String String

let b:current_syntax = "{0}"

