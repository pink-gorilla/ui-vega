# ui-vega [![GitHub Actions status |pink-gorilla/ui-vega](https://github.com/pink-gorilla/ui-vega/workflows/CI/badge.svg)](https://github.com/pink-gorilla/ui-vega/actions?workflow=CI)[![Codecov Project](https://codecov.io/gh/pink-gorilla/ui-vega/branch/master/graph/badge.svg)](https://codecov.io/gh/pink-gorilla/ui-vega)[![Clojars Project](https://img.shields.io/clojars/v/org.pinkgorilla/ui-vega.svg)](https://clojars.org/org.pinkgorilla/ui-vega)

## ui.vega
- ui-vega defines a reagent wrapper to render vega-plots
- vega is a browser based plot renderer, that uses declarative syntax to build plots
- vega comes as vega spec and vega-lite spec. vega lite spec is compiled to vega-spec 
and is a more condensed specification with less features.

## ui.vega.plot
- is a simple data-driven plotting dsl.
- provides a few functions to quickly generate vega specs, and
  therefore vega plots.

## Demos  (port : 8000)

Run `clj -X:goldly` to see ui-vega goldly snippets. Navigate to snippets registry.


## Implementation

- Legacy gorilla-plot was originally written by Jony Hudson for Gorilla repl.
- Gorilla-Plot Legacy uses vega-spec, updated to vega 5 spec.
- multiplot uses vega-lite spec.

## Performance test

```
clj -M:perf
```

## Unit test

```
clj -M:test
./scripts/test-cljs.sh
```

**vega problems**

If there are problems in using vega with errors to "buffer" then `npm install shadow-cljs --save` might fix it. thheller: both buffer and process are polyfills packages that shadow-cljs will provide ... the npm package is mostly the for CLI stuff but also brings in some extra npm packages
the compiler is from the CLJ dependency you have in project.clj.
the npm stuff never does any actual compilation, just runs the java process

you do not need to worry about process or buffer at all
you can fix this easily by bumping 
:compiler-options {:output-feature-set :es6} or whatever language level is appropriate
:es8 is good if you have bunch of async/await code in libs

With UNPKG you can see output of npm builds. Useful for debugging.
https://unpkg.com/browse/vega-embed@6.19.1/build/vega-embed.js