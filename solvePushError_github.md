# [Git] Git Push 에러 해결하기 (error: failed to push some refs to...)


### $ git push 할때, 오류가 남
###### 오류 메세지

```
 failed to push some refs to 'https://github.com/HyeJu99/mint-choco-2020/'
```

> 원인: remote 버전과 로컬의 버전이 달라서 충돌(Conflict)이 된 것

- - -

### 해결 방법
- remote에 있는 변경 사항을 pull로 내려받은 후,
- pull 받으면, 새로운 변경사항은 기본적으로 git이 자동 병합(auto merge)작업을 해줌
- 내 파일과 merge(병합, 합치기) 하여,
- 다시 push로 올림

1. $ git pull (remore name) (branch name)
```
$ git pull origin master
```
2. 충돌된 파일을 열어 수정
> (<<<<<<< HEAD 부터 ======= 사이에 표시된 부분) 가 로컬의 내용
>
> (======= 부터 >>>>>>> a2f34950a0d63f35696cb34e1774b610dfc7d4d3 사이에 표시된 부분)가 remote 에서 pull 받은 내용
>
> 원하는 코드로 수정
3. add
```
$ git add .
```
4. commit
```
$ git commit -m "충돌 해결"
```
5. push
```
$ git push (remore name) (branch name)

//예
$ git push origin master
```
