// These constants are injected via webpack environment variables.
// You can add more variables in webpack.common.js or in profile specific webpack.<dev|prod>.js files.
// If you change the values in the webpack config files, you need to re run webpack to update the application

export const VERSION = process.env.VERSION;
export const DEBUG_INFO_ENABLED = Boolean(process.env.DEBUG_INFO_ENABLED);
export const SERVER_API_URL = process.env.SERVER_API_URL ?? '';
export const BUILD_TIMESTAMP = process.env.BUILD_TIMESTAMP;


export const COMPANIES: any = [1, 2, 3, 4, 5, 6, 7, 8, 9];
export const COMPANY_NAMES: any = { 1: 'IMC', 2: 'GIS', 3: 'PTS', 4: 'AIS', 5: 'OIS', 6: 'H&M', 7: 'DNJ', 8: 'PDS', 9: 'IGS' };