const path = require("path");
const Dotenv = require('dotenv-webpack');

module.exports = {
    mode: "development",
    context: path.join(__dirname, "src"),
    entry: `./index.tsx`,
    output: {
        filename: "main.js",
        path: path.join(__dirname, "dist"),
        clean: {
            keep: /index.html/,
        },
    },
    plugins: [
        new Dotenv()
    ],

    module: {
        rules: [
            {
                test: /\.(js|ts|tsx)?$/,
                use: "ts-loader",
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
            },
            {
                test: /\.(png|jpe?g|gif)$/i,
                type: 'asset/resource',
                generator: {
                    filename: 'assets/[name]-[hash][ext]',
                },
            },
        ]
    },

    resolve: {
        extensions: [".ts", ".tsx", ".js", ".json"]
    },

    devtool: "hidden-source-map",
    devServer: {
        static: {
            directory: path.join(__dirname, "dist"),
        },
    }
}