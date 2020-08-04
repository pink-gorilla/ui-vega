# Gorilla Plot [![GitHub Actions status |pink-gorilla/gorilla-plot](https://github.com/pink-gorilla/gorilla-plot/workflows/CI/badge.svg)](https://github.com/pink-gorilla/gorilla-plot/actions?workflow=CI)[![Codecov Project](https://codecov.io/gh/pink-gorilla/gorilla-plot/branch/master/graph/badge.svg)](https://codecov.io/gh/pink-gorilla/gorilla-plot)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/gorilla-plot.svg)](https://clojars.org/org.pinkgorilla/gorilla-plot)

## GorillaPlot DSL
- gorilla-plot is a simple data-driven plotting dsl.
- vega is a browser based plot renderer, that uses declarative syntax to build plots
- vega comes as vega spec and vega-lite spec. vega lite spec is compiled to vega-spec 
and is a more condensed specification with less features.
- gorilla-ui defines a reagent wrapper to render vega-plots
- gorilla-plots provides a few functions to quickly generate vega specs, and
  therefore vega plots.

## Demo

```
lein notebook
```

This will run pinkgorilla notebook as a library, with gorilla-plot demo notebooks 
loaded into the explorer.


## Implementation

- Legacy gorilla-plot was originally written by Jony Hudson for Gorilla repl.
- Gorilla-Plot Legacy uses vega-spec, updated to vega 5 spec.
- multiplot uses vega-lite spec.

## Performance test

```
lein perf
```

