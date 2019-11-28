# Duct module.logging

[![Build Status](https://travis-ci.org/duct-framework/module.logging.svg?branch=master)](https://travis-ci.org/duct-framework/module.logging)

A [Duct][] module that adds logging to a configuration, using the
[logger.timbre][] library.

[duct]: https://github.com/duct-framework/duct
[logger.timbre]: https://github.com/duct-framework/logger.timbre

## Installation

To install, add the following to your project `:dependencies`:

    [duct/module.logging "0.5.0"]

## Usage

To add this module to your configuration, add a the
`:duct.module/logging` key to your configuration:

```clojure
{:duct.module/logging {}}
```

The module adds the `:duct.logger/timbre` logger to the configuration,
and sets up different appenders depending on whether the environment
is `:development` or `:production`

The environment can be set by the top-level `:duct.core/environment`
key:

```clojure
{:duct.core/environment :production}
```

Or by adding an `:environment` key to the logging module:

```clojure
{:duct.module/logging {:environment :development}}
```

In production, the full logs are sent to `STDOUT`. In development, the
logs are sent to the `logs/dev.log` file, and in a very terse form to
`STDOUT`.

## License

Copyright © 2019 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
