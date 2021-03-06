NAME   	:= civisblockchain/iterop-sign-doc
IMG    	:= ${NAME}:${VERSION}
LATEST  := ${NAME}:latest
build:
	@docker build --build-arg VERSION=${VERSION} -f Dockerfile -t ${IMG} .

tag-latest:
	@docker tag ${IMG} ${LATEST}

push:
	@docker push ${NAME}

