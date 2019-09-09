import axios from 'axios';

export function getIntroduction() {
    return axios.get('http://localhost:8080/api/introductions');
}