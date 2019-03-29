NAME   	:= civisblockchain/sign-doc
IMG    	:= ${NAME}:${VERSION}

build:
	@docker build --build-arg VERSION=${VERSION} -f Dockerfile -t ${IMG} .

push:
	@docker push ${IMG}