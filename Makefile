.PHONY: all clean clean_pdf run_interactive

# default: all

NAME = thesis

help:
	@echo "Please use \`make <target>' where <target> is one of"
	@echo "  all              to make all pdf files"
	@echo "  clean            to delete all generated files"
	@echo "  clean_pdf        to delete the pdf file"
	@echo "  clean_all        to delete all generated files and the pdf file"
	@echo "  run_interactive  to run latexmk in interactive mode"

all: compile_figs
	# pdflatex -shell-escape -interaction=nonstopmode -file-line-error ./$(NAME).tex
	pdflatex -shell-escape -file-line-error ./$(NAME).tex
	bibtex $(NAME)
	pdflatex -shell-escape -file-line-error ./$(NAME).tex
	pdflatex -shell-escape -file-line-error ./$(NAME).tex

compile_figs:
	pdflatex -shell-escape -file-line-error -output-directory=./figs/FM ./figs/FM/background_featuremodel.tex
	pdflatex -shell-escape -file-line-error -output-directory=./figs/FM ./figs/FM/background_featuremodel.tex
	pdflatex -shell-escape -file-line-error -output-directory=./figs/FM ./figs/FM/background_featuremodel.tex

clean_all: clean clean_pdf

clean: clean_figs
	rm -rf *.aux *.bbl *.blg *.log *.out *.spl *.synctex.gz *.toc *.bcf *.run.xml *.nav *.snm *.vrb *.fdb_latexmk *.fls .$(NAME)-cache

clean_figs:
	rm -rf ./figs/FM/*.pdf ./figs/FM/*.aux ./figs/FM/*.bbl ./figs/FM/*.blg ./figs/FM/*.log ./figs/FM/*.out ./figs/FM/*.spl ./figs/FM/*.synctex.gz ./figs/FM/*.toc ./figs/FM/*.bcf ./figs/FM/*.run.xml ./figs/FM/*.nav ./figs/FM/*.snm ./figs/FM/*.vrb ./figs/FM/*.fdb_latexmk ./figs/FM/*.fls ./figs/FM/.{{NAME}}-blx.bib ./figs/FM/.{{NAME}}-cache

clean_pdf:
	rm -f $(NAME).pdf

run_interactive: clean clean_figs compile_figs
	latexmk -pvc -pdf -shell-escape -interaction=nonstopmode -file-line-error ./$(NAME).tex

