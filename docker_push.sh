version=$( cat version )
next_version=$(($version + 1))
echo $next_version > version
docker build -t auth-server:lastest .
docker tag auth_server jusuk/auth-server:$next_version
docker push jusuk/auth-server:$next_version