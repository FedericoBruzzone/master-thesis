# Variables
NAME := "thesis"

# Recipes
_default:
  just --list --justfile {{justfile()}}

# Build the pdf file
all:
  @echo "Making all pdf files"
  @just pdflatex
  @just bibtex
  @just pdflatex
  @just pdflatex

# Build the pdf file
build:
  @echo "Making all pdf files"
  @just pdflatex
  @just bibtex
  @just pdflatex
  @just pdflatex

# Build the pdf file once
pdflatex:
  # @pdflatex -shell-escape -interaction=nonstopmode -file-line-error ./{{NAME}}.tex
  @pdflatex -shell-escape -file-line-error ./{{NAME}}.tex

# Build the bibtex file
bibtex:
  @bibtex {{NAME}}

# Clean up generated files and the pdf file
clean_all:
  @just clean
  @just clean_pdf

# Clean up generated files
clean:
  @echo "Cleaning up generated files"
  @rm -rf *.aux *.bbl *.blg *.log *.out *.spl *.synctex.gz *.toc *.bcf *.run.xml *.nav *.snm *.vrb *.fdb_latexmk *.fls .{{NAME}}-blx.bib .{{NAME}}-cache

# Clean up the pdf file
clean_pdf:
  @echo "Cleaning up pdf file"
  @rm -f {{NAME}}.pdf

# Run latexmk in interactive mode
run_interactive:
  @echo "Running latexmk in interactive mode"
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
