.PHONY: run
run:
	docker run --rm \
	-v $(PWD)/default.conf:/etc/nginx/conf.d/default.conf:ro \
	-v $(PWD)/api/:/usr/share/nginx/html/api/:ro \
	-p 8888:8888 \
	nginx:stable-alpine

