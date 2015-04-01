out ?= build

uberjar = target/defbot.jar

all: $(uberjar)
.PHONY: all

$(uberjar): project.clj src/** resources/**
	lein with-profile nix uberjar

install: $(uberjar)
	mkdir -pv $(out)
	cp -rv $(uberjar) $(out)
	cp -rv bin $(out)/bin
	cp security.policy $(out)
.PHONY: install
