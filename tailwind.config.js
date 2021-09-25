module.exports = {
	mode: "jit",
	purge: [
		"./src/main/kotlin/**/*.java",
		"./src/main/webapp/WEB-INF/**/*.{jsp,jspf,html}",
	],
	darkMode: 'class',
	variants: {
		extend: {},
	},
	plugins: [
		require('daisyui'),
	],
};