
SRCDIRS := pages components modules test
#NOTE: MACs didn't like -regex :(
SRCFILES := server.js $(shell find $(SRCDIRS) -name '*.jsx' -o -name '*.js')

CONFIG := ./.prettierrc
PRETTY := npx prettier --config $(CONFIG)

PRE-COMMIT-PATH := ./.git/hooks/pre-commit

# The name of all staged files.
# This ugly eval business is to make sure that this variable is only
# expanded when needed and only expanded once on the first reference
STAGED-SRCFILES = $(eval STAGED-SRCFILES := $$(filter $(SRCFILES),$(shell git diff --cached --name-only --diff-filter=ACM)))$(STAGED-SRCFILES)

.PHONY: help
help:
	@echo give me a rule

# Verify whether all source files are correctly formatted
.PHONY: format-check
format-check:
	@$(PRETTY) --check $(SRCFILES); exit 0

# Format all files
.PHONY: format-all
format-all:
	@$(PRETTY) --write $(SRCFILES)

# Format only staged files
.PHONY: format-staged
format-staged:
	$(if $(STAGED-SRCFILES),@echo formatting some files; $(PRETTY) --write $(STAGED-SRCFILES) && git add $(STAGED-SRCFILES),@echo There were no files to format)

# add a pre-commit hook to run 'make format-staged' automatically
# every time 'git commit' is run (or whenever something is committed i guess)
.PHONY: install-pre-commit
install-pre-commit:
	echo -e '#!/bin/sh\nmake format-staged' > $(PRE-COMMIT-PATH)
	chmod +x $(PRE-COMMIT-PATH)

# as an alternative to the above
# make commit m:='commit message'
.PHONY: commit
commit: format-staged
	git commit $(if $(m),-m '$(m)')

# Runs createDB.sql script, creating the kvelit database if it doesn't exist
.PHONY: create-database
create-database:
	@ echo "Creating database"
	@ read -p "Enter user: " user; \
	  mysql -u $$user -p < sql/createDB.sql

# Runs populate.sql, adding dummy data to the database
.PHONY: populate-database
populate-database:
	@ echo "Creating database"
	@ read -p "Enter user: " user; \
	  mysql -u $$user -p < sql/populate.sql
