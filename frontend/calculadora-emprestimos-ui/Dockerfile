# ====================
# PRIMEIRA FASE (BUILD)
# ====================
# Usa Node.js como imagem base para compilar o React
FROM node:20-alpine as build

# Define o diretório de trabalho
WORKDIR /app

# Copia package.json e package-lock.json/yarn.lock
COPY package*.json ./

# Instala as dependências (incluindo dependências de desenvolvimento)
RUN npm install

# Copia o código-fonte
COPY . .

# Comando de build do React/Vite (cria a pasta 'dist' ou 'build')
RUN npm run build


# ====================
# SEGUNDA FASE (RUNTIME - NGINX)
# ====================
# Usa Nginx, que é uma imagem muito leve e rápida para servir arquivos estáticos
FROM nginx:alpine

# Copia os arquivos estáticos compilados (da pasta 'dist') para a pasta do Nginx
# Ajuste 'dist' para 'build' se o seu comando npm run build gerar uma pasta 'build'
COPY --from=build /app/dist /usr/share/nginx/html

# A porta 80 é a porta padrão que o Nginx usa
EXPOSE 80

# Comando padrão do Nginx para rodar o servidor
CMD ["nginx", "-g", "daemon off;"]