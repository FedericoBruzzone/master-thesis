# Variables
NAME := "thesis"

# Recipes
_default:
  just --list --justfile {{justfile()}}

# Build the pdf file
all:
  @just pdflatex
  @just bibtex
  @just pdflatex
  @just pdflatex

# Build the pdf file
build:
  @just pdflatex
  @just bibtex
  @just pdflatex
  @just pdflatex

# Build the pdf file once
pdflatex: compile_figs
  # @pdflatex -shell-escape -interaction=nonstopmode -file-line-error ./{{NAME}}.tex
  @pdflatex -shell-escape -file-line-error ./{{NAME}}.tex

# Compile the figures
compile_figs:
  @pdflatex -shell-escape -file-line-error -output-directory=./figs/FM ./figs/FM/background_featuremodel.tex
  @pdflatex -shell-escape -file-line-error -output-directory=./figs/FM ./figs/FM/background_featuremodel.tex
  @pdflatex -shell-escape -file-line-error -output-directory=./figs/FM ./figs/FM/background_featuremodel.tex

# Build the bibtex file
bibtex:
  @bibtex {{NAME}}

# Clean up generated files and the pdf file
clean_all:
  @just clean
  @just clean_figs
  @just clean_pdf

# Clean up generated files
clean: clean_figs
  @rm -rf *.aux *.bbl *.blg *.log *.out *.spl *.synctex.gz *.toc *.bcf *.run.xml *.nav *.snm *.vrb *.fdb_latexmk *.fls .{{NAME}}-blx.bib .{{NAME}}-cache

clean_figs:
  @rm -rf ./figs/FM/*.pdf ./figs/FM/*.aux ./figs/FM/*.bbl ./figs/FM/*.blg ./figs/FM/*.log ./figs/FM/*.out ./figs/FM/*.spl ./figs/FM/*.synctex.gz ./figs/FM/*.toc ./figs/FM/*.bcf ./figs/FM/*.run.xml ./figs/FM/*.nav ./figs/FM/*.snm ./figs/FM/*.vrb ./figs/FM/*.fdb_latexmk ./figs/FM/*.fls ./figs/FM/.{{NAME}}-blx.bib ./figs/FM/.{{NAME}}-cache

# Clean up the pdf file
clean_pdf:
  @rm -f {{NAME}}.pdf

# Run latexmk in interactive mode
run_interactive: clean clean_figs compile_figs
  @latexmk -pvc -pdf -shell-escape -interaction=nonstopmode -file-line-error ./{{NAME}}.tex

_help:
  @echo "Usage: just <recipe>"
  @echo ""
  @echo "Available recipes:"
  @echo "  all             # Build the pdf file"
  @echo "  build           # Build the pdf file"
  @echo "  pdflatex        # Build the pdf file once"
  @echo "  bibtex          # Build the bibtex file"
  @echo "  clean_all       # Clean up generated files and the pdf file"
  @echo "  clean           # Clean up generated files"
  @echo "  clean_pdf       # Clean up the pdf file"
  @echo "  run_interactive # Run latexmk in interactive mode"
