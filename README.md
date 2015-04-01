# defbot

A [slack](http://www.slack.com) bot for clojure.

## Usage

This project provides packaging for [nix](http://nixos.org/nix), so
that should cover 0.00001% of all users.

### If you're using nix

```
$ nix-env -f . -i defbot
...

$ defbot
usage: defbot [TOKEN]

Start a defbot using slack API token TOKEN.
```

### If you're not using nix

You can build an uberjar and run it as follows:

```
$ lein uberjar
...

$ java -jar target/defbot.jar
usage: defbot [TOKEN]

Start a defbot using slack API token TOKEN.
```

### Hacking

```
$ lein repl

...

user=> (require 'defbot.bot)
nil
user=> (in-ns 'defbot.bot)
nil
defbot.bot=> (run-bot! "slack-api-token")
```

## License

Copyright © 2015 Ragnar Dahlen

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
