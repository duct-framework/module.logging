# Duct Logging Module [![Build Status](https://github.com/duct-framework/module.logging/actions/workflows/test.yml/badge.svg)](https://github.com/duct-framework/logger.simple/actions/workflows/test.yml)

A [Duct][] module that adds logging to a configuration, using the
[logger.simple][] library.

This current version is experimental and will only work with the new
[duct.main][] tool. The artifact group name has been changed to prevent
accidental upgrades.

[duct]: https://github.com/duct-framework/duct
[logger.simple]: https://github.com/duct-framework/logger.simple
[duct.main]: https://github.com/duct-framework/duct.main

## Installation

Add the following dependency to your deps.edn file:

    org.duct-framework/module.logging {:mvn/version "0.6.6"}

Or to your Leiningen project file:

    [org.duct-framework/module.logging "0.6.6"]

## Usage

Add the `:duct.module/logging` key to your Duct configuration:

```edn
{:duct.module/logging {}}
```

This module uses the Integrant [expand][] function to add the
`:duct.logger/simple` logger to the configuration. It will configured
differently depending on the active Integrant profile:

- `:repl` - write all logs to "logs/repl.log" and `:report` level logs to
            STDOUT in brief
- `:test` - write all logs to "logs/test.log"
- `:main` - write all logs in full to STDOUT

[expand]: https://github.com/weavejester/integrant#expanding

## License

Copyright © 2025 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
