module.exports = {
    devServer: {
      proxy: {
        '^/api': {
          target: 'http://localhost:8070/',
          changeOrigin: true
        },
      }
    }
  }