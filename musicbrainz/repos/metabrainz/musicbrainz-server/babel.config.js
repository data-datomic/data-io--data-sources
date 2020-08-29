const BROWSER_TARGETS = {
  chrome: '49',
  edge: '14',
  firefox: '52',
  ie: '11',
  safari: '9.0',
};

const NODE_TARGETS = {
  node: process.versions.node,
};

module.exports = function (api) {
  api.cache.using(() => process.env.NODE_ENV);

  const presets = [
    ['@babel/preset-env', {
      corejs: 3,
      targets: api.caller(caller => caller && caller.target === 'node')
        ? NODE_TARGETS
        : BROWSER_TARGETS,
      useBuiltIns: 'usage',
    }],
  ];

  const plugins = [
    '@babel/plugin-transform-flow-strip-types',
    '@babel/plugin-transform-react-jsx',
    '@babel/plugin-transform-react-constant-elements',
    ['@babel/plugin-transform-runtime', {
      corejs: 3,
      helpers: true,
      proposals: true,
      regenerator: true,
      useESModules: false,
    }],
    '@babel/plugin-proposal-class-properties',
    '@babel/plugin-syntax-dynamic-import',
    '@babel/plugin-proposal-optional-chaining',
    '@babel/plugin-proposal-nullish-coalescing-operator',
  ];

  if (process.env.NODE_ENV === 'test') {
    plugins.push('babel-plugin-istanbul');
  }

  const ignore = [
    'node_modules',
    'root/static/scripts/tests/typeInfo.js',
    /root\/static\/build\/jed-[A-z_-]+?\.source\.js$/,
  ];

  return {
    presets,
    plugins,
    ignore,
    sourceType: 'unambiguous',
  };
};
