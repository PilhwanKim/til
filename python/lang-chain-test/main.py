from dotenv import load_dotenv
import os
import openai
from langchain_openai import ChatOpenAI
from langchain.prompts import PromptTemplate
from langchain_core.output_parsers import StrOutputParser


# API KEY 정보로드
load_dotenv()

# print(f"[API KEY]\n{os.environ['OPENAI_API_KEY']}")
# print(f"[openai version]\n{openai.__version__}")

# template 정의
# template = "{country}의 수도는 어디인가요?"
template = """
당신은 영어를 가르치는 10년차 영어 선생님입니다. 상황에 [FORMAT]에 영어 회화를 작성해 주세요.

상황:
{question}

FORMAT:
- 영어 회화:
- 한글 해석:
"""

# from_template 메소드를 이용하여 PromptTemplate 객체 생성
prompt_template = PromptTemplate.from_template(template)

# prompt 생성
# prompt = prompt_template.format(country="미국")


model = ChatOpenAI(
    model="gpt-3.5-turbo",
    max_tokens=2048,
    temperature=0.1,
)

output_parser = StrOutputParser()

chain = prompt_template | model | output_parser
print(chain.invoke({"question": "저는 식당에 가서 음식을 주문하고 싶어요"}))
