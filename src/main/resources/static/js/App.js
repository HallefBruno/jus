/* global Vue */

const Dashboard = {
    data() {
        return {
           dados:{},
           listaDados:[]
        };
    },
    methods: {
        add() {
            this.listaDados.push(this.dados);
            this.dados = {};
        }
    },
    created() {
        
    }
};

Vue.createApp(Dashboard).mount("#app");